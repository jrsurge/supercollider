/*
    SC_Levenshtein.h
    Copyright (c) 2019 James Surgenor

    This is a header-only, templated functor to compute the Levenshtein distance between two arrays.

    It uses the reduced memory footprint version, based on Martin Ettl's implementation, and is written as a functor
    to facilitate even greater memory savings if a single instance is used (although only one comparison can be made
    at a time). It is self-contained, so could easily be multithreaded.

    Thanks to Brian Heim

    ====================================================================

    SuperCollider real time audio synthesis system
    Copyright (c) 2002 James McCartney. All rights reserved.
    http://www.audiosynth.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
*/

#pragma once

#include <vector>
#include <functional>

template <typename data_type> struct levenshteinDistance {
    int operator()(
        const data_type* dataA, size_t sizeA, const data_type* dataB, size_t sizeB,
        std::function<bool(const data_type&, const data_type&)> compareFunc =
            [](const data_type& a, const data_type& b) { return a == b; }) {
        // empty array optimisation
        if (sizeA == 0)
            return sizeB;
        if (sizeB == 0)
            return sizeA;

        // reduce memory by comparing against the smallest array
        if (sizeA < sizeB) {
            const data_type* dataC = dataA;
            dataA = dataB;
            dataB = dataC;

            size_t sizeC = sizeA;
            sizeA = sizeB;
            sizeB = sizeC;
        }


        matrix.resize(sizeB + 1);

        size_t indX = 0;
        size_t indY = 0;

        // initialise matrix
        for (auto& val : matrix) {
            val = indY;
            indY++;
        }

        // calculate the distances
        for (indX = 0; indX < sizeA; ++indX) {
            matrix[0] = indX + 1;
            size_t corner = indX;

            for (indY = 0; indY < sizeB; ++indY) {
                size_t upper = matrix[indY + 1];

                if (compareFunc(dataA[indX], dataB[indY]))
                    matrix[indY + 1] = corner;
                else
                    matrix[indY + 1] = min3(upper, corner, matrix[indY]) + 1;

                corner = upper;
            }
        }

        return matrix[sizeB];
    }

private:
    std::vector<size_t> matrix;

    const size_t& min3(const size_t& a, const size_t& b, const size_t& c) {
        // returning const ref extends lifetime of temporary
        const size_t& d = a < b ? a : b;
        return c < d ? c : d;
    }
};
